#praatxml
This is an app for converting Praats native format -textgrid- to xml which can be used more widely than textgrids.
There is no backward conversion from xml to textgrid. Sources are provided as .cljx, the main files in src/cljs and src/clj are for web and desktop respective. 
I used this in a Phonetic project for writing specialized, high-performance scripts in Python to extract the data.
## Usage
Just compile the jar or use it under http://ifisher.pythonanywhere.com/projects/praatxml.
Then:
	java -jar praatxml.jar example.TextGrid #ending can be lowercase
or with path
	java -jar praatxml.jar ~/phonetics/example.TextGrid #ending can be lowercase
or for directories:
	java -jar praatxml.jar ./
The app writes in same dir as the parsed file  - only with a .xml ending.


FIXME
+For now supports only interval tiers
+disable pretty print on demand
+Needs some tests
+Web app should also read uploads as input.


## License

Copyright © 2014 Igor Fischer

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
