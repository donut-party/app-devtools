(ns donut.app-devtools.generators-test
  (:require
   [clojure.edn :as edn]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [donut.app-devtools.generators :as sut]
   [donut.generate :as dg]))

(def deps-edn
  "{:deps {org.clojure/clojure {:mvn/version \"1.12.0\"}}

 :donut/scripts
 {:default [\"dev\" \"tailwind:watch\"]
  :scripts {\"dev\"            {:shadow-build :dev}
            \"tailwind:watch\" {:cmd [\"npx\" \"tailwindcss\" \"--watch\"]}}}}")

(def shadow-cljs-edn
  "{:dev-http {3000 {:roots [\"resources/public\" \"frontend-target/dev/public\"]}}
 :builds   {:dev {}}}")

(def system-ns
  "(ns my-app.backend.system
  (:require
   [donut.system :as ds]))

(defmethod ds/named-system :base
  [_]
  {::ds/defs {}})")

(deftest install-test
  (let [[deps scripts default-scripts roots target system-require system-plugin]
        (dg/generate
         ::sut/install
         {:top             "my-app"
          :gen-lib-version "0.0.1"}
         {:read-point  (dg/read-point-test-fn
                        {::sut/deps.edn                deps-edn
                         ::sut/deps.edn-script         deps-edn
                         ::sut/deps.edn-default-script deps-edn
                         ::sut/shadow-cljs.edn-roots   shadow-cljs-edn
                         ::sut/shadow-cljs.edn-target  shadow-cljs-edn
                         ::sut/system-require          system-ns
                         ::sut/system-plugin           system-ns})
          :write-point dg/write-point-test})]

    (testing "adds app-devtools dep to deps.edn"
      (is (= "deps.edn" (:file-path deps)))
      (is (= {:mvn/version "0.0.1"}
             (get-in (edn/read-string (:contents deps)) [:deps 'party.donut/app-devtools]))))

    (testing "declares the app-devtools shadow build as a :donut/scripts script"
      (is (= {:shadow-build :app-devtools}
             (get-in (edn/read-string (:contents scripts)) [:donut/scripts :scripts "app-devtools"]))))

    (testing "runs the app-devtools script by default"
      (is (= ["dev" "tailwind:watch" "app-devtools"]
             (get-in (edn/read-string (:contents default-scripts)) [:donut/scripts :default]))))

    (testing "adds the app-devtools output to the dev-http roots"
      (is (= ["resources/public" "frontend-target/dev/public" "frontend-target/app-devtools/public"]
             (get-in (edn/read-string (:contents roots)) [:dev-http 3000 :roots]))))

    (testing "adds an app-devtools build that compiles under bakery's embedded shadow-cljs"
      (let [build (get-in (edn/read-string (:contents target)) [:builds :app-devtools])]
        (is (= 'donut.app-devtools.frontend.core/init (get-in build [:modules :app-devtools :init-fn])))
        (is (= #{'donut.bakery.inject} (get-in build [:compiler-options :cache-blockers])))
        (is (not (contains? build :build-hooks)))))

    (testing "adds app-devtools plugin require to system ns"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  (str/replace-first system-ns
                                            "[donut.system :as ds])"
                                            "[donut.system :as ds]\n   [donut.app-devtools.backend.plugin :as app-devtools-plugin])")}
             system-require)))

    (testing "adds app-devtools plugin to base system"
      (is (= {:file-path "src/my_app/backend/system.clj"
              :contents  (str/replace-first system-ns
                                            "{::ds/defs {}}"
                                            "{::ds/defs {}\n   :donut.system/plugins [app-devtools-plugin/app-devtools-plugin]}")}
             system-plugin)))))
