@echo off
set USERDIR=C:\job-ppt2img\webroot
set CLASSPATH=.;%JAVA_HOME%\lib
set CLASSPATH=%CLASSPATH%;%USERDIR%\WEB-INF\classes;%USERDIR%\WEB-INF\lib\*
echo %CLASSPATH%
java -classpath %CLASSPATH% -Djava.library.path=C:\Java\jdk1.7.0_17\bin com.elc.job.ppt2img.task.PPT2ImgTask %*