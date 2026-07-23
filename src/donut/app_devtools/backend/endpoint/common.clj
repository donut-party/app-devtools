(ns donut.app-devtools.backend.endpoint.common
  (:require
   [clojure.walk :as walk]))

(defn format-system-element
  [x]
  (if ((some-fn vector? map? seq? number? string? keyword? set?) x)
    x
    (str x)))

(defn format-system
  "Handle objects that don't lend themselves to transit transmission, like functions"
  [system]
  (walk/postwalk format-system-element system))
