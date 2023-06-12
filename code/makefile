sources = NurseSolver.java nurses/*.java constraintevals/*.java Attributes/*.java Helper/*.java

nurses.jar: $(sources)
	javac NurseSolver.java
	jar cvmf manifest.mf nurses.jar NurseSolver.class */*.class
