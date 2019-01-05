class Logger {
    info(message) {
        console.log(`[INFO]    ${message}`)
    }

    debug(message) {
        console.log(`[DEBUG]   ${message}`)
    }

    warning(message) {
        console.log(`[WARNING] ${message}`)
    }

    error(message) {
        console.log(`[ERROR]   ${message}`)
    }
}

export default new Logger()
