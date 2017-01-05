//kompilacja programu w javie
javac Paleta.java

//generowanie pliku naglowkowego
javah -jni Paleta

//tworzenie biblioteki dynamicznej
gcc -I /Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/include -I /Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/include/darwin -c Paleta.c
gcc -dynamiclib -o libpaleta.jnilib Paleta.o

//uruchomienie 
java Paleta
