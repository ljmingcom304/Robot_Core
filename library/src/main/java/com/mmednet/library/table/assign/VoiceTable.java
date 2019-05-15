package com.mmednet.library.table.assign;


import java.util.List;

/**
 * Title:VoiceTable
 * <p>
 * Description:实现该接口可以实现语音赋值
 * </p>
 * Author Jming.L
 * Date 2018/11/12 16:16
 */
public interface VoiceTable extends Table {

    /**
     * 是否可编辑，编辑状态下支持语音录入，非编辑状态下不支持语音录入
     *
     * @return 编辑状态
     */
    boolean isEditable();

    /**
     * 是否限制自由输入，若限制自由输入则会调用{@link VoiceTable#getHints()}
     *
     * @return true只能从备选内容中选择;false可以自由输入内容
     */
    boolean isLimit();

    /**
     * 获取提示内容或备选内容
     *
     * @return 提示内容
     */
    List<String> getHints();

}
