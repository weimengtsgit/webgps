package com.sjw.util;

import cn.net.sosgps.memcache.Memcache;
import cn.net.sosgps.memcache.MemcacheFactory;
import cn.net.sosgps.memcache.bean.Ent;
import cn.net.sosgps.memcache.bean.Group;
import cn.net.sosgps.memcache.bean.Position;
import cn.net.sosgps.memcache.bean.Terminal;
import cn.net.sosgps.memcache.bean.User;
import cn.net.sosgps.memcache.impl.java_memcached.MemcacheFactoryImpl;

public class Constants {

    private static final MemcacheFactory MEMCACHE_FACTORY = new MemcacheFactoryImpl();

    public static final Memcache<Terminal> TERMINAL_CACHE = MEMCACHE_FACTORY.getCache("t_terminal",
            Terminal.class);

    public static final Memcache<User> USER_CACHE = MEMCACHE_FACTORY.getCache("t_user", User.class);

    public static final Memcache<Group> GROUP_CACHE = MEMCACHE_FACTORY.getCache("t_group",
            Group.class);

    public static final Memcache<Ent> ENT_CACHE = MEMCACHE_FACTORY.getCache("t_ent", Ent.class);

    public static final Memcache<Position> POSITION_CACHE = MEMCACHE_FACTORY.getCache(
            "loc_position", Position.class);
}
