(ns donut.app-devtools.generators-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [donut.app-devtools.generators :as sut]
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

    (testing "adds app-devtools dep to deps.edn"
      (is (= {:file-path "deps.edn"
              :contents  "{:deps {org.clojure/clojure {:mvn/version \"1.12.0\"}
        party.donut/app-devtools {:mvn/version \"0.0.1\"}}}"}
             deps-edn)))

    (testing "adds app-devtools build target to shadow-cljs.edn"
      (is (= "shadow-cljs.edn" (:file-path shadow-cljs)))
      (is (str/includes? (:contents shadow-cljs) ":app-devtools\n"))
      (is (str/includes? (:contents shadow-cljs) ":init-fn donut.app-devtools.frontend.core/init")))

    (testing "adds app-devtools plugin require to system ns"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]
   [donut.app-devtools.plugin :as app-devtools-plugin]))"}
             system-require)))

    (testing "adds app-devtools plugin to base system"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))

(defmethod ds/named-system :base
  [_]
  {::ds/defs {}
   :donut.system/plugins [app-devtools-plugin/app-devtools-plugin]})"}
             system-plugin)))

    (testing "adds app-devtools to package.json shadow:watch script"
      (is (= {:file-path "package.json"
              :contents  "{\n  \"scripts\" : {\n    \"shadow:watch\" : \"shadow-cljs watch dev app-devtools\"\n  }\n}"}
             package-json)))))

(deftest add-app-devtools-to-shadow-watch-test
  (is (= "shadow-cljs watch dev app-devtools"
         (sut/add-app-devtools-to-shadow-watch "shadow-cljs watch dev" "app-devtools")))
  (testing "does not add app-devtools twice"
    (is (= "shadow-cljs watch dev app-devtools"
           (sut/add-app-devtools-to-shadow-watch "shadow-cljs watch dev app-devtools" "app-devtools")))))
