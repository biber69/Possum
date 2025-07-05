import jenkins.model.*
import hudson.model.*

Thread.start {
    sleep(10000)

    def jobName = "vault-app"
    def job = Jenkins.instance.getItem(jobName)

    if (job) {
        println "🚀 Автозапуск job '${jobName}'"
        job.scheduleBuild2(0)
    } else {
        println "⚠️ Job '${jobName}' не найдена"
    }
}
