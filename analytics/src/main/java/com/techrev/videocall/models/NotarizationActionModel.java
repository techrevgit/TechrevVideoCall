package com.techrev.videocall.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotarizationActionModel {
    private static NotarizationActionModel instance;

    private List<NotarizationActions> notarizationActions;

    private NotarizationActionModel() {
        // Private constructor to prevent instantiation from outside
    }

    public static synchronized NotarizationActionModel getInstance() {
        if (instance == null) {
            instance = new NotarizationActionModel();
        }
        return instance;
    }

    public List<NotarizationActions> getNotarizationActions() {
        return notarizationActions;
    }

    public void setNotarizationActions(List<NotarizationActions> notarizationActions) {
        this.notarizationActions = notarizationActions;
    }

    public class NotarizationActions {
        @SerializedName("notarizationActionId")
        private String notarizationActionId;

        @SerializedName("actionName")
        private String actionName;

        @SerializedName("actionId")
        private String actionId;

        @SerializedName("actionDescription")
        private String actionDescription;

        public String getNotarizationActionId() {
            return notarizationActionId;
        }

        public void setNotarizationActionId(String notarizationActionId) {
            this.notarizationActionId = notarizationActionId;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public String getActionDescription() {
            return actionDescription;
        }

        public void setActionDescription(String actionDescription) {
            this.actionDescription = actionDescription;
        }
    }
}
