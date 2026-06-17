# donut bakery

Developer tools:

- system explorer
- db info

## dev

start app in `example` directory to see it at work

view at http://localhost:3000/donut/bakery

## using bakery

TODO better explain

could be a generator?

update shadow-cljs config:

- new build named `:bakery`
- config like below

``` clojure
;; shadow-cljs configuration
{:deps           {:aliases [:cljs :dev :framework-dev]}
 :dev-http       {3000 {:roots     ["resources/public" "frontend-target/dev/public" "frontend-target/bakery/public"]
                        :proxy-url "http://localhost:3010"
                        :push-state/headers {"Cross-Origin-Opener-Policy" "same-origin-allow-popups"}}}

 :builds {:dev
          {:target           :browser
           :output-dir       "frontend-target/dev/public/js"
           :asset-path       "/js"
           :compiler-options {:optimizations           :none
                              :source-map              true
                              :source-map-detail-level :all
                              :closure-defines         {re-frame.trace.trace-enabled?        true
                                                        day8.re-frame.tracing.trace-enabled? true}
                              ;; this is specifically for the warning in loom/alg_generic:494.
                              :warnings                {:invalid-arithmetic false}}
           :build-hooks      [(donut.frontend.routes/write-frontend-routes-hook bakery.example.frontend.frontend-routes/routes)]

           :modules
           {:main {:entries [bakery.example.frontend.core]
                   :init-fn bakery.example.frontend.core/init}}

           ;; start a development http server on http://localhost:3000
           :devtools
           {:preloads        [day8.re-frame-10x.preload
                              devtools.preload]
            :hud             #{:errors}
            :reload-strategy :full
            :ignore-warnings true}}

          :bakery
          {:target           :browser
           :output-dir       "frontend-target/bakery/public/js"
           :asset-path       "/js"
           :compiler-options {:optimizations           :none
                              :source-map              true
                              :source-map-detail-level :all
                              :closure-defines         {re-frame.trace.trace-enabled?        true
                                                        day8.re-frame.tracing.trace-enabled? true}
                              ;; this is specifically for the warning in loom/alg_generic:494.
                              :warnings                {:invalid-arithmetic false}}
           :build-hooks      [(donut.frontend.routes/write-frontend-routes-hook bakery.example.frontend.frontend-routes/routes)]

           :modules
           {:bakery {:entries [donut.bakery.frontend.core]
                     :init-fn donut.bakery.frontend.core/init}}

           ;; start a development http server on http://localhost:3000
           :devtools
           {:preloads        [day8.re-frame-10x.preload
                              devtools.preload]
            :hud             #{:errors}
            :reload-strategy :full
            :ignore-warnings true}}}}

```

update package.json:

``` json
"shadow:watch": "shadow-cljs watch dev bakery ext",
```

## License

MIT

Copyright © 2026 Daniel Higginbotham

