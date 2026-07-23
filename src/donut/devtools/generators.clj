(ns donut.devtools.generators
  (:require
   [clojure.string :as str]
   [donut.generate :as dg]
   [donut.system :as-alias ds]
   [rewrite-clj.zip :as rz]))

(defn add-devtools-to-shadow-watch
  [base s]
  (let [els (set (str/split base #"\s"))]
    (if (els s)
      base
      (str base " " s))))

(defmethod dg/generator ::install
  [_ {:keys [top] :as data}]
  (let [system-ns (symbol (str (dg/file-path->ns top) ".backend.system"))]
    {:points      [{:id          ::deps.edn
                    :type        [:clojure :modify]
                    :description "update deps.edn, add latest party.donut/devtools"
                    :destination {:path "deps.edn"}
                    :modify      {:path  [:deps (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/interpolated "{party.donut/devtools {:mvn/version \"{{gen-lib-version}}\"}}")]]}}
                   {:id          ::shadow-cljs.edn-roots
                    :type        [:clojure :modify]
                    :description "update shadow-cljs.edn, add devtools target to roots"
                    :destination {:path "shadow-cljs.edn"}
                    :modify      {:path  [:dev-http :roots (dg/pred vector?)]
                                  :edits [[rz/append-child "frontend-target/devtools/public"]]}}
                   {:id          ::shadow-cljs.edn-target
                    :type        [:clojure :modify]
                    :description "update shadow-cljs.edn, add devtools build target"
                    :destination {:path "shadow-cljs.edn"}
                    :modify      {:path  [:builds (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/clj-template-resource "donut/devtools/generator-templates/shadow-cljs-build.edn")]]}}
                   {:id          ::system-require
                    :type        [:clojure :modify]
                    :description "add devtools plugin require to system ns"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['ns (dg/find-parent :require)]
                                  :edits [dg/append-child-newline
                                          [rz/append-child '[donut.devtools.backend.plugin :as devtools-plugin]]]}}
                   {:id          ::system-plugin
                    :type        [:clojure :modify]
                    :description "add devtools plugin to base system"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['defmethod 'ds/named-system :base (dg/pred map?)]
                                  :edits [[dg/upsert-vector-key :donut.system/plugins 'devtools-plugin/devtools-plugin]]}}
                   {:id          ::package.json
                    :type        [:json :modify]
                    :description "update package.json to include devtools"
                    :destination {:path "package.json"}
                    :modify      {:path  [:scripts (keyword "shadow:watch")]
                                  :edits [[add-devtools-to-shadow-watch "devtools"]]}}]
     :data-schema [:map
                   [:top {:optional? false}]]
     :data        data}))
