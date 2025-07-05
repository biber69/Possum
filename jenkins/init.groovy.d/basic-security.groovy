import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
instance.setSecurityRealm(new HudsonPrivateSecurityRealm(false))
instance.setAuthorizationStrategy(new FullControlOnceLoggedInAuthorizationStrategy())
instance.save()
