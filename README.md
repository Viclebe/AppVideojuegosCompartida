Otrappartida

Gestor de Colecciones de Videojuegos y comunidad Gamer

Otrappartida es una aplicación móvil nativa para Android desarrollada con **Jetpack Compose** que permite a los usuarios gestionar su catálogo personal de videojuegos, 
interactuar con una comunidad global de gamers en tiempo real, seguir sus progresos individuales (títulos, favoritos, estados de juego) y dejar valoraciones o comentarios.

El proyecto se implementa con una arquitectura basada en la persistencia híbrida (SQL local
+ NoSQL en la nube), lo que garantiza un funcionamiento offline combinado con el dinamismo de
la nube.

Cuentas de prueba preconfiguradas

Para facilitar la evaluación y testeo de la aplicación, el sistema ya cuenta con los siguientes perfiles creados en **Firebase Authentication** y 
sincronizados en la base de datos:

Usuario 1 email: victor@email.com Contraseña: 123123
Usuario 2 email: eider@email.com Contraseña: 123123
Usuario 3 email: luz@email.com Contraseña: 123123

Para poner en marcha la aplicación en tu Android Studio, sigue estos pasos:

1. Abrir el proyecto: Abre Android Studio, dale a **File > Open** y selecciona esta carpeta (`AppVideojuegos`).
2. Sincronizar Gradle: Deja que carguen todos los archivos e indexe el proyecto. Si no empieza solo, dale al botón del elefante arriba a la derecha (*Sync Project with Gradle Files*).
3. Firebase: Ya he dejado el archivo `google-services.json` configurado y metido dentro de la carpeta `app/`, así que no hace falta configurar nada de Firebase para que funcione 
la base de datos y el login.
4. Lanzar la app: Conecta tu móvil por USB (con la depuración USB activada) o abre un emulador en Android Studio. Dale al botón verde de **Play** (Run) arriba y listo. Te saldrá 
la pantalla de carga (Splash) y luego la pantalla de login.
