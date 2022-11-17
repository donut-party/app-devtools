(ns donut.bakery.backend.endpoint.endpoint-routes-endpoint)

(def collection-parameters
  {:path [:map
          ;; example: [:todo_list/id int?]
          ]})

(def collection-handlers
  {:get
   {:parameters collection-parameters
    :handler
    (fn [{:keys [routes]}]
      {:status 200
       :body   [{:endpoint-routes/id :all
                 :endpoint-routes/routes routes}]})}})
