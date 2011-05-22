package com.api.deployer.notifications;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.notifications
 */
public enum NotificationType {
	INFO("Informational", 1),
	WARNING("Warnings", 2),
	ERROR("Errors", 3),
	DEBUG("Debug", 0);

    private String message;
    private int level;

    private NotificationType( String message, int level ) {
        this.message = message;
        this.level = level;
    }

    public String message() {
        return this.message;
    }

    public int level() {
        return this.level;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
