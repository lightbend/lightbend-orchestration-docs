# Lightbend Orchestration Documentation

This project contains the documentation for Lightbend Orchestration. This is a [Paradox](https://github.com/lightbend/paradox) project that will be published to [Lightbend Tech Hub](https://developer.lightbend.com/).

## Project Status

Lightbend Orchestration is no longer actively developed and will reach its [End of Life](https://developer.lightbend.com/docs/lightbend-platform/introduction/getting-help/support-terminology.html#eol) on April 15, 2020.

We recommend [Migrating to the Improved Kubernetes Deployment Experience](https://developer.lightbend.com/docs/lightbend-orchestration/current/migration.html).

## Build

```bash
sbt paradox
```

or for continuous processing use `~paradox` within sbt

```bash
$ sbt
~paradox
```

## Preview

```bash
sbt run
```

## Reference

The Lightbend style uses Zurb Foundation for layout.  The basic media query concepts are
to support "small", "medium", "large" screens, and lay things out in a 12 column grid.

For example, you can place an image within the layout grid like:
```html
<img src="some.png" "small-5 medium-4 large-3">
```

which means roughly: "take 5/12th of the screen on phone screens, 4/12th on medium tablet screens,
and 3/12th of the screen for laptops"

other examples:
```html
<img src="some.png" "small-10 medium-7 large-5">
<img src="some.png" "small-12 large-9 float-center">
<img src="some.png" "small-12 medium-10 large-7">
<img src="some.png" "small-12 medium-9 large-6">
```

As a test, you can resize the browser on a large monitor, shrinking the window to preview
the medium and small layouts.  Our style hides the side menus when it reaches the small
breakpoint.

references:
* https://foundation.zurb.com/sites/docs/media-queries.html
* https://github.com/zurb/foundation-sites

## Release

Consult "Lightbend Orchestration Release Process" on Google Drive
