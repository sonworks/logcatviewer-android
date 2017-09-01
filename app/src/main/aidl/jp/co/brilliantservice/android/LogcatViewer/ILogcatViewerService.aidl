package jp.co.brilliantservice.android.LogcatViewer;

interface ILogcatViewerService
{
    java.util.List<String> getDispData();
    int saveLog( String FileName );
    void setLogBreak();
}
