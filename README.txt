Build requires Maven 3

1. Run 'mvn eclipse:to-maven -DstripQualifier=true -DeclipseDir=<eclipse_install>' to copy eclipse library files to to local maven repository.
2. From the projects root diretory run 'mvn clean package eclipse:eclipse' to generate the MANIFEST.MF and the Eclipse Project files.
3. To bring the project into Eclipse use File -> Import -> General -> Existing Project Into Workspace.
4. After making changes to the dependencies you will need to re-run 'mvn clean package' and refresh the project in Eclipse.
