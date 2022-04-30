package net.prismc.prishub.utils;

public enum FontInfo {

    A('a', '\uE800'),
    B('b', '\uE801'),
    C('c', '\uE802'),
    D('d', '\uE803'),
    E('e', '\uE804'),
    F('f', '\uE805'),
    G('g', '\uE806'),
    H('h', '\uE807'),
    I('i', '\uE808'),
    J('j', '\uE809'),
    K('k', '\uE80A'),
    L('l', '\uE80B'),
    M('m', '\uE80C'),
    N('n', '\uE80D'),
    O('o', '\uE80E'),
    P('p', '\uE80F'),
    Q('q', '\uE810'),
    R('r', '\uE811'),
    S('s', '\uE812'),
    T('t', '\uE813'),
    U('u', '\uE814'),
    V('v', '\uE815'),
    W('w', '\uE816'),
    X('x', '\uE817'),
    Y('y', '\uE818'),
    Z('z', '\uE819'),
    NUM0('0', '\uE81A'),
    NUM1('1', '\uE81B'),
    NUM2('2', '\uE81C'),
    NUM3('3', '\uE81D'),
    NUM4('4', '\uE81E'),
    NUM5('5', '\uE81F'),
    NUM6('6', '\uE820'),
    NUM7('7', '\uE821'),
    NUM8('8', '\uE822'),
    NUM9('9', '\uE823'),
    SLASH('/', '\uE824'),
    APOSTROPHE('\'', '\uE825'),
    PERIOD('.', '\uE826'),
    SPACE(' ', '\uE827');

    private final char OLD;
    private final char NEW;

    FontInfo(char old, char aNew) {
        OLD = old;
        NEW = aNew;
    }

    public char getCharacter() {
        return OLD;
    }

    public char getReplacement() {
        return NEW;
    }
}
