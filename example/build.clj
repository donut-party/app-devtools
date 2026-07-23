(ns build
  "app-devtools/example's build script. 

  Run tests:
  clojure -X:test
  For more information, run:
  clojure -A:deps -T:build help/doc"

  (:require
   [clojure.tools.build.api :as b])
  (:refer-clojure :exclude [test]))

(def lib 'app-devtools/example)
(def version (format "0.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (delay (b/create-basis {:project "deps.edn"})))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[app-devtools.example.backend.core]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'app-devtools.example.backend.core}))
