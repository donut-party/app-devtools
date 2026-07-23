(ns donut.devtools.generators-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [donut.devtools.generators :as sut]
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

    (testing "adds devtools dep to deps.edn"
      (is (= {:file-path "deps.edn"
              :contents  "{:deps {org.clojure/clojure {:mvn/version \"1.12.0\"}
        party.donut/devtools {:mvn/version \"0.0.1\"}}}"}
             deps-edn)))

    (testing "adds devtools build target to shadow-cljs.edn"
      (is (= "shadow-cljs.edn" (:file-path shadow-cljs)))
      (is (str/includes? (:contents shadow-cljs) ":devtools\n"))
      (is (str/includes? (:contents shadow-cljs) ":init-fn donut.devtools.frontend.core/init")))

    (testing "adds devtools plugin require to system ns"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]
   [donut.devtools.plugin :as devtools-plugin]))"}
             system-require)))

    (testing "adds devtools plugin to base system"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))

(defmethod ds/named-system :base
  [_]
  {::ds/defs {}
   :donut.system/plugins [devtools-plugin/devtools-plugin]})"}
             system-plugin)))

    (testing "adds devtools to package.json shadow:watch script"
      (is (= {:file-path "package.json"
              :contents  "{\n  \"scripts\" : {\n    \"shadow:watch\" : \"shadow-cljs watch dev devtools\"\n  }\n}"}
             package-json)))))

(deftest add-devtools-to-shadow-watch-test
  (is (= "shadow-cljs watch dev devtools"
         (sut/add-devtools-to-shadow-watch "shadow-cljs watch dev" "devtools")))
  (testing "does not add devtools twice"
    (is (= "shadow-cljs watch dev devtools"
           (sut/add-devtools-to-shadow-watch "shadow-cljs watch dev devtools" "devtools")))))
