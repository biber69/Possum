import jenkins.model.*
import hudson.model.*
import java.net.*

def instance = Jenkins.getInstance()
def jobName = "vault-app"

Thread.start {
    sleep(10000) // ждём Jenkins

    // Ждём Vault
    def vaultReady = false
    for (int i = 0; i < 5; i++) {
        try {
            def conn = new URL("http://vault:8200/v1/sys/health").openConnection()
            conn.setConnectTimeout(2000)
            conn.setReadTimeout(2000)
            def code = conn.responseCode
            if (code == 200 || code == 429) {
                vaultReady = true
                println "🔓 Vault готов (HTTP $code)"
                break
            } else {
                println "⏳ Vault отвечает, но статус $code... ждём"
            }
        } catch (Exception e) {
            println "❌ Vault ещё не доступен: ${e.getMessage()}"
        }
        sleep(3000)
    }

    if (!vaultReady) {
        println "🛑 Vault так и не поднялся. Job не запускаем."
        return
    }

    def job = instance.getItem(jobName)

    if (job != null) {
        println "🚀 Запускаем job '${jobName}'"
        job.scheduleBuild2(0)
    } else {
        println "⚠️ Job '${jobName}' не найдена"
    }
}
