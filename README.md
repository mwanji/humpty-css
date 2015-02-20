# humpty-css

Provides minification and URL rewriting for CSS files in the humpty pipeline.

## Installation

In Maven:

````xml
<dependency>
  <groupId>co.mewf.humpty</groupId>
  <artifactId>humpty-css</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
````

## URL rewriting

Rewrites url imports to paths that can be served by Servlet 3's resource mechanism. For example, in Bootstrap:

````css
src: url('../fonts/glyphicons-halflings-regular.eot');
````

is rewritten to

````css
src:url('/webjars/bootstrap/3.2.0/fonts/glyphicons-halflings-regular.eot');
````

The latter can be served directly by a [Servlet 3.0 container](http://alexismp.wordpress.com/2010/04/28/web-inflib-jarmeta-infresources/).

## Compression

Uses an inline version of [YUI Compressor](http://yui.github.io/yuicompressor/) to avoid dragging in YUI Compressor's JavaScript-related dependencies.

## Configuration

### Name

css

### Options

Option|Default|Description
------|-------|-----------
minify|only in production mode|Set to true or false to force minification to be always on or off, respectively
rewrite|true|Set to false to disable URL rewriting

### Example

````toml
[options.css]
  minify = true
````