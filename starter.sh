echo "                   API GridDeploy Distribution            ";
echo "";

APPS_DIR=`pwd`;
JAVA_EXEC="java -jar";

help() {
	echo "Available commands:";
	echo "--------------------------------";
	echo "";
	echo "help - Show help contents";
	echo "";
	echo "start [agent|server|ui] \$resourcesdir - Start specified service under resources directory specified as context";
	echo "";
	echo "stop [agent|server] - Stop specified service";
	echo "";
}

if [ "$1" = "help" ]; then
	help;
elif [ "$1" = "start" ]; then
	shift;
	CONTEXT_DIR=$APPS_DIR/$1
	EXECUTABLE=$CONTEXT_DIR/$1.jar
	PARAMS="resources/context.xml"
	if [ -f $CONTEXT_DIR"/resources/rmi.xml" ]; then
		PARAMS=$PARAMS" resources/rmi.xml";
	fi;

	if [ -f $EXECUTABLE ]; then
		echo "Changing current directory to: $CONTEXT_DIR";
		cd $CONTEXT_DIR;
		echo "Starting $EXECUTABLE with params $PARAMS";
		nohup $JAVA_EXEC $EXECUTABLE $PARAMS > "$1.log";
	else
		echo "Unknown command requested...";
		exit 0;
	fi;
else
	help;
fi;

echo "--------------------------------";
echo "        API Ltd. 2010(c)";
echo "--------------------------------";
	
exit 1;
