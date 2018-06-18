node {
	stage('Clone test repo')
		git url:'https://github.com/mateuszgruszczynski/gatlingperf2018.git', branch: 'develop'

	stage('Run tests')
	    withCredentials([string(credentialsId: 'GRAFANA_TOKEN', variable: 'GRAFANA_TOKEN')]) {
            sh "export BASE_LOAD=${BASE_LOAD}"
            sh "export BASE_DURATION=${BASE_DURATION}"
            sh "export BASE_URL=${BASE_URL}"
            sh "export GRAPHITE_HOST=${GRAPHITE_HOST}"
            def annotationText = "Performance tests executed: <a href='${BUILD_URL}'>${BUILD_TAG}</a>"
            def annotationBody = '''{"time":''' + System.currentTimeMillis() +''', "isRegion":true, "tags":["test","perf"], "text":"''' + annotationText + '''" }'''
            def response = httpRequest acceptType: 'APPLICATION_JSON', contentType: 'APPLICATION_JSON', customHeaders: [[maskValue: false, name: 'Authorization', value: "${GRAFANA_TOKEN}"]], httpMode: 'POST', requestBody: annotationBody, url: "http://${GRAPHITE_HOST}:3000/api/annotations"
            def annotation = readJSON text: response.content
            def annotationId = annotation.id
            def sbtHome = tool 'sbt-default'
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
                //sh "${sbtHome}/bin/sbt 'gatling:testOnly ${SIMULATION}' || true"
                sh "${sbtHome}/bin/sbt 'gatling:test' || true"
            }
            def annotationUpdate = annotationBody.replace("{", '''{ "timeEnd":''' + (System.currentTimeMillis() + 10000) + ", ")
            println annotationUpdate
            def updateResponse = httpRequest acceptType: 'APPLICATION_JSON', contentType: 'APPLICATION_JSON', customHeaders: [[maskValue: false, name: 'Authorization', value: "${GRAFANA_TOKEN}"]], httpMode: 'PUT', requestBody: annotationUpdate, url: "http://${GRAPHITE_HOST}:3000/api/annotations/" + annotation.id
        }

    stage('Archive artifacts')
        gatlingArchive()
		archiveArtifacts artifacts: 'target/gatling/**'
		deleteDir()
}