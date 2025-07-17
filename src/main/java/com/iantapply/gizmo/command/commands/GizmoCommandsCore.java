package com.iantapply.gizmo.command.commands;

import com.iantapply.gizmo.GizmoRenewed;

/**
 * Handles all Gizmo related subcommands
 */
public class GizmoCommandsCore {

    public void initialize() {
        GizmoRenewed.getInstance().getCommandCore().stageCommand(new GizmoSubcommand());
    }
}
