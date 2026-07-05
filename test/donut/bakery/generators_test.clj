(ns donut.bakery.generators-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [donut.bakery.generators :as sut]
   [donut.generate :as dg]))

(deftest install-test
  (let [[deps-edn shadow-cljs system-require system-plugin package-json]
        (dg/generate
         ::sut/install
         {:top             "my-app"
          :gen-lib-version "0.0.1"}
         {:read-point      (dg/read-point-test-fn
                            {::sut/deps.edn        "{:deps {org.clojure/clojure {:mvn/version \"1.12.0\"}}}"
                             ::sut/shadow-cljs.edn "{:builds {:dev {}}}"
                             ::sut/system-require  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))"
                             ::sut/system-plugin   "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))

(defmethod ds/named-system :base
  [_]
  {::ds/defs {}})"})
          :read-json-point (dg/read-json-point-test-fn
                            {::sut/package.json "{\"scripts\": {\"shadow:watch\": \"shadow-cljs watch dev\"}}"})
          :write-point     dg/write-point-test})]

    (testing "adds bakery dep to deps.edn"
      (is (= {:file-path "deps.edn"
              :contents  "{:deps {org.clojure/clojure {:mvn/version \"1.12.0\"}
        party.donut/bakery {:mvn/version \"0.0.1\"}}}"}
             deps-edn)))

    (testing "adds bakery build target to shadow-cljs.edn"
      (is (= "shadow-cljs.edn" (:file-path shadow-cljs)))
      (is (str/includes? (:contents shadow-cljs) ":bakery\n"))
      (is (str/includes? (:contents shadow-cljs) ":init-fn donut.bakery.frontend.core/init")))

    (testing "adds bakery plugin require to system ns"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]
   [donut.bakery.plugin :as bakery-plugin]))"}
             system-require)))

    (testing "adds bakery plugin to base system"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))

(defmethod ds/named-system :base
  [_]
  {::ds/defs {}
   :donut.system/plugins [bakery-plugin/bakery-plugin]})"}
             system-plugin)))

    (testing "adds bakery to package.json shadow:watch script"
      (is (= {:file-path "package.json"
              :contents  "{\n  \"scripts\" : {\n    \"shadow:watch\" : \"shadow-cljs watch dev bakery\"\n  }\n}"}
             package-json)))))

(deftest add-bakery-to-shadow-watch-test
  (is (= "shadow-cljs watch dev bakery"
         (sut/add-bakery-to-shadow-watch "shadow-cljs watch dev" "bakery")))
  (testing "does not add bakery twice"
    (is (= "shadow-cljs watch dev bakery"
           (sut/add-bakery-to-shadow-watch "shadow-cljs watch dev bakery" "bakery")))))
