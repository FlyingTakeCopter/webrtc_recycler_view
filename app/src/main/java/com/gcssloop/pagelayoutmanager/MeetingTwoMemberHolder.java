package com.gcssloop.pagelayoutmanager;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * two member holder
 *
 * @author lqk
 */
public class MeetingTwoMemberHolder extends AbstractMeetingHolder {

    /**
     * local in which view
     */
    private static final int LOCAL_IN_BIG = 0;
    private static final int LOCAL_IN_SMALL = 1;

    /**
     * 大屏
     */
    MemberVideoView bigView;
    /**
     * 小屏
     */
    MemberVideoView smallView;
    /**
     * show state
     */
    private int state = LOCAL_IN_BIG;

    MeetingTwoMemberHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void init(View view){
        bigView = view.findViewById(R.id.big_view);
        smallView = view.findViewById(R.id.small_view);

        bigView.setBigView(true);
    }

    @Override
    public void update(RenderData data) {
        if (!data.isLocal()){
            throw new RuntimeException("MeetingTwoMemberHolder update error: is not local");
        }
        if (data.getRemoteData() != null){
            smallView.setVisibility(View.VISIBLE);
            if (state == LOCAL_IN_BIG){
                bigView.update(data);
                smallView.update(data.getRemoteData());
            }else {
                bigView.update(data.getRemoteData());
                smallView.update(data);
            }
        }else {
            state = LOCAL_IN_BIG;
            bigView.update(data);
            smallView.recycle();
            smallView.setVisibility(View.GONE);
        }
    }

    @Override
    public void recycle() {

    }

    @Override
    public void bindView() {
        bigView.bindVideo();
        smallView.bindVideo();
    }

    @Override
    public void unBindView() {
        bigView.unBindVideo();
        smallView.unBindVideo();
    }
}
