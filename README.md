# WalkWithMeAnalytics
Walk With Me App adaptada para la obtencion de datos a traves del acelerometro de los dispositivos Bean, incluye funciones basicas:
- Configuracion de parametros para la toma de datos. 
- Captura de datos y almacenamiento en formato CSV.


# Requisitos
- Android 5.0 o superior.
- Dispositivos Punchthrough Bean. 
- Conexion BLE 4.0 

# APK disponible en URL
http:// .... 

# Manual de uso. 
Prender dispositivo y presionar en la imagen de la aplicacion, se mostrara una lista con los dispositivos disponibles, al seleccionar el deseado se abriran las opciones para tomar muestra.
#### Nombre
Ingresar el nombre que se le asignara a la captura de muestras, se generara un archivo con el nombre ingresado y el alias de la fecha y hora en el que fue tomado, ejemplo: **nombreMuestra-30122018235959.csv** 
#### Tiempo 
El tiempo debe ser ingresado en milisegundos, el minimo puede ser 5000 que equivale a 5 segundos.

Milisegundos | Segundos
------------ | -------------
5000 milisegundos | 5 segundos
10000 milisegundos | 10 segundos
60000 milisegundos | 60 segundos
120000 milisegundos | 120 segundos - 2 minutos

#### Intervalo
Valor por defecto y minimo | 200
------------ | -------------

El intervalo de tiempo es requerido para el timer. **200** es el minimo valor probado y se ha obtenido como resultado, 5 lecturas del acelerometro por segundo. Aumentando este valor se disminuira la cantidad de datos por segundo. **Este resultado puede variar con la finalidad de reducir la cantidad de datos por segundo.**

#### Boton Iniciar y Cancelar
Estos botones se activaran de acuerdo a la funcion que se encuentre realizando la aplicacion, al configurar los parametros exitosamente, se activara el boton **Iniciar**, al presionar el boton se convertira en el timer de la duracion de la toma de datos y se activara el boton **Cancelar** para anular la toma de datos, al finalizar el tiempo se guardara la informacion en la carpeta csvFiles ubicada en los archivos de Android. 
