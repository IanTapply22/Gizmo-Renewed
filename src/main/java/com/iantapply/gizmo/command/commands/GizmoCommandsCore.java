package com.iantapply.gizmo.command.commands;

import com.iantapply.gizmo.Gizmo;

/**
 * Handles all Gizmo related subcommands
 */
public class GizmoCommandsCore {

    public void initialize() {
        Gizmo.getInstance().getCommandCore().stageCommand(new GizmoSubcommand());
    }
}
