sources = NurseSolver.java nurses/*.java constraintevals/*.java Attributes/*.java Helper/*.java

nurses.jar: $(sources)
	rm NurseSolver.class nurses/*.class constraintevals/*.class
	javac NurseSolver.java
	jar cvmf manifest.mf nurses.jar NurseSolver.class */*.class
