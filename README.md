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

**`donut.bakery.frontend.core` gets its backend routes via
`donut.compiler.monolith.inject/backend-routes`** (see
`../donut-compiler/03-single-jvm/README.md`) — the consuming app must be
set up per that design: `RouteRegistryComponent` in its backend system,
`party.donut/compiler-monolith` on its classpath, and a single JVM
running both the backend and an embedded shadow-cljs watch. There's no
more `donut.generated.endpoint-routes` file to generate — bakery no
longer works with the older generated-file route-sharing setup.

update shadow-cljs config:

- new build named `:bakery`
- config like below (note `:cache-blockers` — required on every build
  that compiles `donut.bakery.frontend.core`, same as any other
  namespace that uses the inject macro)

``` clojure
;; shadow-cljs configuration
{:deps           {:aliases [:cljs :dev :framework-dev :monolith]}
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
                              :warnings                {:invalid-arithmetic false}
                              :cache-blockers          #{donut.compiler.monolith.inject}}
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
                              :warnings                {:invalid-arithmetic false}
                              :cache-blockers          #{donut.compiler.monolith.inject}}
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

update package.json — watch both builds under the monolith entrypoint
(build ids are trailing CLI args, see
`../donut-compiler/03-single-jvm/README.md`):

``` json
"dev": "clojure -M:dev:cljs:framework-dev:monolith -m donut.compiler.monolith.dev dev bakery",
```

## License

MIT

Copyright © 2026 Daniel Higginbotham

