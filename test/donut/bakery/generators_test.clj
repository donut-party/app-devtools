(ns donut.bakery.generators-test
  (:require
   [clojure.test :refer [deftest is]]
   [donut.generate :as dg]
   [donut.bakery.generators :as sut]))

(deftest migrations-test
  (let [generated (dg/generate ::sut/install
                               {:top "generate-test"}
                               {:write-point dg/write-point-test})]
    (is (= [{:file-path "tmp/test-bakery"
             :contents  "test template"}]
           generated))))
