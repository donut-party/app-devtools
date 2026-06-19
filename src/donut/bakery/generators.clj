(ns donut.bakery.generators
  (:require
   [donut.generate :as dg]))

(defmethod dg/generator ::install
  [_ _]
  {:points      [{:id          ::test
                  :description "test generator"
                  :destination {:path "tmp/test-bakery"}
                  :data        {}
                  :content     {:form "test template"}}]
   :data-schema [:map
                 [:top {:optional? false}]]
   :data        {}})
