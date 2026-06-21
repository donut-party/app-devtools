(ns donut.bakery.generators
  (:require
   [clojure.java.io :as io]
   [donut.generate :as dg]))

(defmethod dg/generator ::install
  [_ {:keys [top] :as data}]
  {:points      [{:id          ::deps.edn
                  :description "update deps.edn, add latest party.donut/bakery"
                  :destination {:path "deps.edn"}
                  :modify      {:path  [:deps (dg/pred map?)]
                                :edits [dg/node-merge]}
                  :content     {:template "{party.donut/bakery {:mvn/version \"{{gen-lib-version}}\"}}"}
                  :data        data}
                 {:id          ::shadow-cljs.edn
                  :description "update shadow-cljs.edn, add bakery build target"
                  :destination {:path "shadow-cljs.edn"}
                  :modify      {:path  [:builds (dg/pred map?)]
                                :edits [dg/node-merge]}
                  :content     {:template (slurp (io/resource "donut/bakery/generator-templates/shadow-cljs-build.edn"))}}
                 #_
                 {:id          ::system.clj
                  :description "add bakery plugin to system.clj"
                  :destination {:namespace (symbol (str (dg/->ns top) ".backend.system"))
                                :extension "clj"
                                :dir       "src"}
                  :modify      {:path  ['defmethod 'ds/named-system :dev (dg/pred map?)]
                                :edits [dg/node-merge]}}]
   :data-schema [:map
                 [:top {:optional? false}]]
   :data        data})
