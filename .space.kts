job("Warmup data for IDEA") {
    // optional
    startOn {
        // run on schedule every day at 5AM
        schedule { cron("0 5 * * *") }
        // run on every commit...
        gitPush {
            // ...but only to the space-attempt branch
            branchFilter {
                +"refs/heads/space-attempt"
            }
        }
    }

    warmup(ide = Ide.Idea) {
        // path to the warm-up script
        scriptLocation = "./dev-env-warmup.sh"
        // use image specified in the devfile
        devfile = ".space/devfile.yaml"
    }
 
    failOn {
        testFailed { enabled = false }
        nonZeroExitCode { enabled = false }
        outOfMemory { enabled = false }

        timeOut {
            runningTimeOutInMinutes = 15
        }
    }
}