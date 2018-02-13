# Platform Tooling Docs

This project contains the documentation for Lightbend's Platform Tooling. This is a [Paradox](https://github.com/lightbend/paradox) project that will be published to [Lightbend Tech Hub](https://developer.lightbend.com/).

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
open target/paradox/site/main/index.html
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

Consult "Platform Tooling Release Process" on Google Drive

## Maintenance

Enterprise Suite Platform Team <es-platform@lightbend.com>
