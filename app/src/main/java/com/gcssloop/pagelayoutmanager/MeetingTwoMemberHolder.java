package com.gcssloop.pagelayoutmanager;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * two member holder
 *
 * @author lqk
 */
public class MeetingTwoMemberHolder extends AbstractMeetingHolder implements View.OnClickListener {

    /**
     * local in which view
     */
    private static final int LOCAL_IN_BIG = 0;
    private static final int LOCAL_IN_SMALL = 1;

    RelativeLayout rlTwoMember;
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
    /**
     * 数据
     */
    private RenderData data;

    MeetingTwoMemberHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void init(View view){
        rlTwoMember = view.findViewById(R.id.rl_tow_memeber);
        bigView = view.findViewById(R.id.big_view);
        smallView = view.findViewById(R.id.small_view);
        smallView.setOnClickListener(this);

        bigView.setBigView(true);
    }

    @Override
    public void update(RenderData data) {
        if (data == null){
            rlTwoMember.setVisibility(View.GONE);
            return;
        }
        this.data = data;

        rlTwoMember.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        changeShowState();
    }

    private void changeShowState(){
        // 切换大小屏状态
        state = state == LOCAL_IN_BIG ? LOCAL_IN_SMALL : LOCAL_IN_BIG;

        update(data);
    }
}
