# example

This is a single-page app built with [Donut](https://donut.party)! 🍩

# dev

Create postgres dbs:

``` shell
createdb donut_minimal_dev
createdb donut_minimal_test
```

Start everything in one JVM (backend, nREPL, shadow-cljs watches for the
`:dev` and `:app-devtools` builds, and tailwind):

``` shell
npm install && npm run dev
```

View the app devtools at
[http://localhost:3000/donut/app-devtools](http://localhost:3000/donut/app-devtools)

## License

FIXME choose a license

Copyright © 2026 Danielhigginbotham
