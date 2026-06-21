(ns donut.bakery.generators
  (:require
   [clojure.java.io :as io]
   [donut.generate :as dg]
   [donut.system :as-alias ds]
   [rewrite-clj.zip :as rz]))

(defn find-ns-aliased-kw
  ([kw-str]
   (fn [loc]
     (find-ns-aliased-kw loc kw-str)))
  ([loc kw-str]
   (rz/find loc rz/next #(= (rz/string %) kw-str))))

(defmethod dg/generator ::install
  [_ {:keys [top] :as data}]
  (let [system-ns (symbol (str (dg/->ns top) ".backend.system"))]
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
                   {:id          ::system-require
                    :description "add bakery plugin require to system ns"
                    :destination {:namespace system-ns 
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['ns (dg/find-parent :require)]
                                  :edits [dg/append-child-newline rz/append-child]}
                    :content     {:form '[donut.bakery.plugin :as bakery-plugin]}}
                   {:id          ::system-plugin
                    :description "add bakery plugin to base system"
                    :destination {:namespace system-ns
                                  :extension "clj"
                                  :dir       "src"}
                    :modify      {:path  ['defmethod 'ds/named-system :base (find-ns-aliased-kw "::ds/plugins") rz/right]
                                  :edits [rz/append-child]}
                    :content     {:form 'bakery-plugin/bakery-plugin}}]
     :data-schema [:map
                   [:top {:optional? false}]]
     :data        data}))
