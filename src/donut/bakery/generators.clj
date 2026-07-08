(ns donut.bakery.generators
  (:require
   [clojure.string :as str]
   [donut.generate :as dg]
   [donut.system :as-alias ds]
   [rewrite-clj.zip :as rz]))

(defn add-bakery-to-shadow-watch
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
                    :description "update deps.edn, add latest party.donut/bakery"
                    :destination {:path "deps.edn"}
                    :modify      {:path  [:deps (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/interpolated "{party.donut/bakery {:mvn/version \"{{gen-lib-version}}\"}}")]]}}
                   {:id          ::shadow-cljs.edn
                    :type        [:clojure :modify]
                    :description "update shadow-cljs.edn, add bakery build target"
                    :destination {:path "shadow-cljs.edn"}
                    :modify      {:path  [:builds (dg/pred map?)]
                                  :edits [[dg/node-merge (dg/clj-template-resource "donut/bakery/generator-templates/shadow-cljs-build.edn")]]}}
                   {:id          ::system-require
                    :type        [:clojure :modify]
                    :description "add bakery plugin require to system ns"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['ns (dg/find-parent :require)]
                                  :edits [dg/append-child-newline
                                          [rz/append-child '[donut.bakery.backend.plugin :as bakery-plugin]]]}}
                   {:id          ::system-plugin
                    :type        [:clojure :modify]
                    :description "add bakery plugin to base system"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['defmethod 'ds/named-system :base (dg/pred map?)]
                                  :edits [[dg/upsert-vector-key :donut.system/plugins 'bakery-plugin/bakery-plugin]]}}
                   {:id          ::package.json
                    :type        [:json :modify]
                    :description "update package.json to include bakery"
                    :destination {:path "package.json"}
                    :modify      {:path  [:scripts (keyword "shadow:watch")]
                                  :edits [[add-bakery-to-shadow-watch "bakery"]]}}]
     :data-schema [:map
                   [:top {:optional? false}]]
     :data        data}))
