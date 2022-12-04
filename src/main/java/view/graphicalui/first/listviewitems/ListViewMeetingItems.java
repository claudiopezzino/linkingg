package view.graphicalui.first.listviewitems;

public enum ListViewMeetingItems {
    MEETING_INFO,
    JOINERS_INFO;

    /*--------------------- inner-enum ---------------------*/
    public enum MeetingInfo{
        LABEL_MEETING_ID,
        LABEL_SCHEDULER,
        MEETING_VIEW,
        TIMETABLE,
        BUTTON_GALLERY;

        /*------------------ inner-inner-enum ------------------*/
        public enum MeetingView{
            CIRCLE_IMAGE,
            LABEL_NAME;

            public Integer getIndex(){
                return this.ordinal();
            }
        }
        /*------------------------------------------------------*/

        /*------------------ inner-inner-enum ------------------*/
        public enum TimeTable{
            LABEL_DATE,
            LABEL_TIME;

            public Integer getIndex(){
                return this.ordinal();
            }
        }
        /*------------------------------------------------------*/

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/


    /*--------------------- inner-enum ---------------------*/
    public enum JoinersInfo{
        LABEL_JOINERS,
        FLOW_PANE_JOINERS,
        CHOICE_AREA;

        /*------------------ inner-inner-enum ------------------*/
        public enum ChoiceArea{
            LABEL_CHOICE,
            RADIO_BUTTON_YES,
            RADIO_BUTTON_NO;

            public Integer getIndex(){
                return this.ordinal();
            }
        }
        /*------------------------------------------------------*/

        public Integer getIndex(){
            return this.ordinal();
        }
    }
    /*------------------------------------------------------*/

    public Integer getIndex(){
        return this.ordinal();
    }
}
