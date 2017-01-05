#include "mynative_Paleta.h" 
#include <stdio.h>

JNIEXPORT void JNICALL Java_mynative_Paleta_printText
(JNIEnv *env, jobject obj, jobject c)
{
    
    //obj odnosi sie do this
    //c odnosi sie do obiektu klasy Kolor
    
    //uzyskiwanie tablicy wszystkich kolorów
    //metoda static Kolor[] values() klasy Kolor
    
    //pobranie obiektu klasy Kolor na podstawie jobject
    jclass cls = (*env)->GetObjectClass(env, c);
    
    //pobranie identyfikatora metody values klasy Kolor
    jmethodID mid = (*env)->GetStaticMethodID(env, cls,
                                              "values", "()[LKolor;");
    //pobranie wyniku zwracanego przez metoda 'values' klasy Kolor
    //tj. tablicy kolorów
    jobjectArray joa = (*env)->CallStaticObjectMethod(env, c, mid);
    
    //pobranie dlugosci tablicy
    int len = (*env)->GetArrayLength(env, joa);
    
    //utworzenie identyfikatorów pól red, green, blue obiektu Kolor
    /*jfieldID fidRed = (*env)->GetFieldID(env, cls, "red", "D");
    jfieldID fidGreen = (*env)->GetFieldID(env, cls, "green", "D");
    jfieldID fidBlue = (*env)->GenFieldID(env, cls, "blue", "D");
     */
    jmethodID midRed = (*env)->GetMethodID(env, cls, "red", "D");
    jmethodID midGreen = (*env)->GetMethodID(env, cls, "green", "D");
    jmethodID midBlue = (*env)->GetMethodId(env, cls, "blue", "D");
    
    printf("Enum type 'Kolor' contains %d elements\n", len);
    
    for(int i=0); i<len; i++) {
        //pobranie i-tego elementu tablicy tj. elementu typu
        //wyliczeniowego Kolor
        jobject elem (*env)->GetObjectArrayElement(env, joa, i);
        
        /* odczytujemy metodami, bo pola prywatne
        jdouble red  = (*env)->GetDoubleField(env, elem, fidRed);
        jdouble green = (*env)->GetDoubleField(env, elem, fidGreen);
        jdouble blue = (*env)->GetDoubleField(env, elem, fidBlue);
         */
        jdouble red = (*env)->CallObjectMethod(env, elem, midRed);
        jdouble green = (*env)->CallObjectMethod(env, elem, midGreen);
        jdouble blue = (*env)->CallObjectMethod(env, elem, midBlue);
        
        //odczytaie nazy kolor elementu elem typu wyliczeniowego Kolor
        jmethodID midName = (*env)->GetMethodID(env, cls,"name",
                                                "()Ljava/lang/String;");
        jstring jstr = (*env)->CallObjectMethod(env, elem midName);
        const char* str = (*env)->GetStringUTFChars(env, jstr, NULL);
        
        //.... wypisanie ...
        
        printf("\t %s\t%g %g %g\t", str, red, green, blue);
        
        (*env)->ReleaseStringUTFChars(env, jstr, str);
    }
}