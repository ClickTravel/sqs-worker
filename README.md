#sqs-worker
This project creates a simple java application containing some work to be done by iron.io. For now this worker simply posts an event to an AWS SQS queue, this event should be handled within the application that is listing to the given queue.

##Create Iron Worker project
From the Iron Worker dashboard add a new project, for instance the project could be called `SQSWorker`. Once the project is created the project id will be displayed, note this down as it will be needed in a moment. Next generate an API key from within the `API Tokens` section of the user profile, also note down this value. Now, set system properties which contain the values just noted:

    export IRON_PROJECT_ID=<your project id>
    export IRON_TOKEN=<your API key>

##Generate Eclipse project
To generate the Eclipse project navigate to `/sqs-worker` and run the following commend:

    ./gradlew eclipse

This will perform a gradle build and generate a `.classpath` and `.project` file in the main `sqs-worker` directory. In Eclipse, the `sqs-worker` project can then be imported as a Java project

##Build
To build this project navigate to `/sqs-worker` and run the following commend:

    ./gradlew oneJar

This will perform a gradle build and generate a jar file in the `sqs-worker/build/libs` directory

##Run locally
Create a file in the main `sqs-worker` directory which contains the payload data, in this example we use `payload.json` but the name isn't important.

Next run the application and pass in the name of the payload file which has just been created:

    java -jar ./build/libs/sqs-worker.jar -payload payload.json

This will run the sqs-worker task code and put the messages provided in the payload file onto the relevant SQS queue. If this is successful then the worker can be packaged up and deployed to iron.io.

##Deploy
Before deploying install the iron_worker CLI onto your machine, instructions here: http://dev.iron.io/worker/reference/cli/. Remember to sudo the install command as this isn't given on the iron.io site, e.g.:

    sudo curl -sSL https://cli.iron.io/install | sh

After building the project it needs to be deployed to iron.io, from `/sqs-worker` run the following commends:
    
    cd ./build/lib
    zip -r sqs-worker.zip sqs-worker.jar 
    cd ../..
    mv ./build/libs/sqs-worker.zip .
    iron worker upload --zip sqs-worker.zip --name sqs-worker iron/java java -jar sqs-worker.jar

`iron worker` will upload the jar file and make it available through the admin console. Log into the iron.io console and click the Worker area of `SQSWorker`. Click the `Code` tab at the top of the screen, this should show `sqs-worker` and the last updated date.

##Configure
To set up a new schedule log into the iron.io console and click the `Worker` area of `SQSWorker`. Click the `Scheduled Tasks` tab at the top of the screen, this should show all currently scheduled tasks then do the following:

- Click the calendar icon to ass a new task:
- Set Code to be `sqs-worker`
- Add a descriptive label
- Set the details of when the task will first run and repeat
- Set the payload, this should be a JSON string and should be in the format detailed below.
- Click schedule task to create the schedule

###JSON Payload
The worker task is generic so has to be configured through the payload posted to it. The payload should take the following structure:

    {
      "tasks": [
        {
          "accessKey": "value",
          "secretKey": "value",
          "region": "value",
          "targetQueue": "value",
          "eventText": "value"
        }
      ]
    }

Where:

- accessKey and secretKey are AWS credentials
- region is one of the AWS regions (e.g. eu-west-1)
- targetQueue is the name of the queue to post a message to
- eventText is the escaped string of the event, this is the same as the message that would be posted through the AWS console

It's also possible to post more than one message at a scheduled time, just add more JSON objects to the tasks list, for instance:

    {
      "tasks": [
        {
          "accessKey": "value",
          "secretKey": "value",
          "region": "value",
          "targetQueue": "value",
          "eventText": "value"
        },
        {
          "accessKey": "value",
          "secretKey": "value",
          "region": "value",
          "targetQueue": "value",
          "eventText": "value"
        }
      ]
    }