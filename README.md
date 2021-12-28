<h1>This is the test task for TUI project</h1>

<h2>Docker</h2>
To build Docker container:

`docker build -t user/testtui .`

To run Docker container:

`docker run -p 8080:8080 user/testtui`

After you can open:

http://localhost:8080/swagger-ui.html

To create AWS CloudFormation Stack :

`aws cloudformation create-stack --stack-name Tui-test --template-body file://src/main/resources/cloudformation/cf.yaml --capabilities CAPABILITY_NAMED_IAM`

