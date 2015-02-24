# blogping
Sample project to ping updated blogs through this Rest web service in Jersey.

API methods available:
* **pingSite** - ping your blog with correct name and url in get/post way;
* **changes.xml** - get changes.xml with updated ping.

## Import & Run
Blogping is a maven project that can be imported by an IDE, following through Eclipse:

* Import: ` File --> Import --> Maven --> Existing Maven Projects in Workspace -> Select root directory -> Done `
* Install [Jetty plugin](http://eclipse-jetty.github.io/installation.html) for Eclipse  
* Run project: through IDE or command line `maven jetty:run`
* Use web service directly on browser [http://localhost:8079/blogping/changes.xml](http://localhost:8079/blogping/changes.xml)

## Examples
*All examples assume you have already setup your environment!*

Ping blog in get:

```
http://localhost:8079/blogping/pingSite?name=djBlog&url=http%3A%2F%2Fdanieleautizi.com%2FblogExample
"Thanks for the ping."
```

Ping blog in post using form addon/plugin:

```
http://localhost:8079/blogping/pingSite

Form params: 
  name=djBlog
  url=http%3A%2F%2Fdanieleautizi.com%2FblogExample
  
"Thanks for the ping."
```

Get ping updates in get:

```
http://localhost:8079/blogping/changes.xml
```
```xml
<weblogUpdates count="1" updated="Tue, 24 Feb 2015 16:46:18 UTC" version="1">
  <weblog name="PetPassion" url="https://www.petpassion.tv/blog" when="2207"/>
</weblogUpdates>
```
