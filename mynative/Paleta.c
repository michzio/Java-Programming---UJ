#include <jni.h> 
#include <stdio.h>
#include <string.h>
#include "Paleta.h"

JNIEXPORT void JNICALL Java_Paleta_printText
(JNIEnv *env, jobject obj, jobject c)
{
    
    //obj odnosi sie do this
    //c odnosi sie do obiektu klasy Kolor
    
    //uzyskiwanie tablicy wszystkich kolorów
    //metoda static Kolor[] values() klasy Kolor
    
    //pobranie obiektu klasy Kolor na podstawie jobject c
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
    jfieldID fidRed = (*env)->GetFieldID(env, cls, "red", "D");
    jfieldID fidGreen = (*env)->GetFieldID(env, cls, "green", "D");
    jfieldID fidBlue = (*env)->GetFieldID(env, cls, "blue", "D");
     /*
    jmethodID midRed = (*env)->GetMethodID(env, cls, "red", "D");
    jmethodID midGreen = (*env)->GetMethodID(env, cls, "green", "D");
    jmethodID midBlue = (*env)->GetMethodID(env, cls, "blue", "D");*/
    
    //kolor biezacego koloru
    jdouble currentRed = (*env)->GetDoubleField(env, c, fidRed);
    jdouble currentGreen = (*env)->GetDoubleField(env, c, fidGreen);
    jdouble currentBlue = (*env)->GetDoubleField(env, c, fidBlue);
    
    //pobranie identyfikatora metody do odczytywania name
    jmethodID midName = (*env)->GetMethodID(env, cls,"name",
                                            "()Ljava/lang/String;");
    //odczytaie nazwy koloru biezacego elementu Kolor przekazanego do
    //tej metody natywnej jako jobject c
    jstring current_jstr = (*env)->CallObjectMethod(env, c, midName);
    const char* current_str = (*env)->GetStringUTFChars(env, current_jstr, NULL);
    
    printf("\t %s %.1g %.1g %.1g\t\n", current_str, currentRed, currentGreen, currentBlue);
    
    printf("Enum type 'Kolor' contains %d elements\n", len);
    
     int i =0;
    for(i=0; i<len; i++) {
        //pobranie i-tego elementu tablicy tj. elementu typu
        //wyliczeniowego Kolor
        jobject elem = (*env)->GetObjectArrayElement(env, joa, i);
        
        
        jdouble red  = (*env)->GetDoubleField(env, elem, fidRed);
        jdouble green = (*env)->GetDoubleField(env, elem, fidGreen);
        jdouble blue = (*env)->GetDoubleField(env, elem, fidBlue);
         
  /*
        jdouble red = (*env)->CallObjectMethod(env, elem, midRed);
        jdouble green = (*env)->CallObjectMethod(env, elem, midGreen);
        jdouble blue = (*env)->CallObjectMethod(env, elem, midBlue);*/
        
        jstring jstr = (*env)->CallObjectMethod(env, elem, midName);
        const char* str = (*env)->GetStringUTFChars(env, jstr, NULL);
        
        //.... wypisanie ...
        if(strcmp(current_str,str) == 0) {
            printf("    ===> %s %.1g %.1g %.1g <===\n", str, red, green, blue);
        } else {
            printf("\t %s %.1g %.1g %.1g\t\n", str, red, green, blue);
        }
        
        (*env)->ReleaseStringUTFChars(env, jstr, str);
    }
    
    (*env)->ReleaseStringUTFChars(env, current_jstr, current_str);
}
