(ns donut.app-devtools.generators
  (:require
   [clojure.string :as str]
   [donut.generate :as dg]
   [donut.system :as-alias ds]
   [rewrite-clj.zip :as rz]))

(defn add-app-devtools-to-shadow-watch
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
                    :description "update deps.edn, add latest party.donut/app-devtools"
                    :destination {:path "deps.edn"}
                    :modify      {:path  [:deps (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/interpolated "{party.donut/app-devtools {:mvn/version \"{{gen-lib-version}}\"}}")]]}}
                   {:id          ::shadow-cljs.edn-roots
                    :type        [:clojure :modify]
                    :description "update shadow-cljs.edn, add app-devtools target to roots"
                    :destination {:path "shadow-cljs.edn"}
                    :modify      {:path  [:dev-http :roots (dg/pred vector?)]
                                  :edits [[rz/append-child "frontend-target/app-devtools/public"]]}}
                   {:id          ::shadow-cljs.edn-target
                    :type        [:clojure :modify]
                    :description "update shadow-cljs.edn, add app-devtools build target"
                    :destination {:path "shadow-cljs.edn"}
                    :modify      {:path  [:builds (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/clj-template-resource "donut/app-devtools/generator-templates/shadow-cljs-build.edn")]]}}
                   {:id          ::system-require
                    :type        [:clojure :modify]
                    :description "add app-devtools plugin require to system ns"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['ns (dg/find-parent :require)]
                                  :edits [dg/append-child-newline
                                          [rz/append-child '[donut.app-devtools.backend.plugin :as app-devtools-plugin]]]}}
                   {:id          ::system-plugin
                    :type        [:clojure :modify]
                    :description "add app-devtools plugin to base system"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['defmethod 'ds/named-system :base (dg/pred map?)]
                                  :edits [[dg/upsert-vector-key :donut.system/plugins 'app-devtools-plugin/app-devtools-plugin]]}}
                   {:id          ::package.json
                    :type        [:json :modify]
                    :description "update package.json to include app-devtools"
                    :destination {:path "package.json"}
                    :modify      {:path  [:scripts (keyword "shadow:watch")]
                                  :edits [[add-app-devtools-to-shadow-watch "app-devtools"]]}}]
     :data-schema [:map
                   [:top {:optional? false}]]
     :data        data}))
