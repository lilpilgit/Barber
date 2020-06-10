#!/bin/sh
MONITORDIR="/home/lilpil/Scrivania/Universita/INGEGNERIA_DEI_SISTEMI_WEB/Prove_Personali/Barber/out/artifacts/Barber_war_exploded/img/products"
DESTINATIONDIR="/home/lilpil/Scrivania/Universita/INGEGNERIA_DEI_SISTEMI_WEB/Prove_Personali/Barber/web/img/products"

#-m to monitor the dir indefinitely, if you don't use this option, once it has detected a new file the script will end.
#--format '%w%f' will print out the file in the format /complete/path/file.name
#"${MONITORDIR}" is the variable containing the path to monitor that we have defined before.
#So in the event that a new file is created,
#inotifywait will detect it and will print the output (/complete/path/file.name) to the pipe
#and while will assign that output to variable NEWFILE.

created_file(){
	inotifywait -m -r -e create --format '%w%f' "${MONITORDIR}" | while read NEWFILE
	do
        	#copy new image of products added to the DESTINATIONDIR
        	#echo "Aggiunta una nuova immagine :" ${NEWFILE}
        	cp "${NEWFILE}" ${DESTINATIONDIR}
	done

}

deleted_file(){
	inotifywait -m -r -e delete --format '%f' "${MONITORDIR}" | while read DELETEDFILE
	do
        	#echo "Cancellata una immagine dal DEPLOY :" ${DELETEDFILE}
		      rm -f ${DESTINATIONDIR}"/""${DELETEDFILE}"
		      #echo "Cancellata una immagine dal DEVELOP :" ${DESTINATIONDIR}"/"${DELETEDFILE}
	done
}

#Run in background 
deleted_file &
created_file &
#wait
