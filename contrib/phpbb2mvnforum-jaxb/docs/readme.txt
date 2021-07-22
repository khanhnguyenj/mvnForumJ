some steps to run phpbb2mvnforum-jaxb:
- go to {phpbb2mvnforum-jaxb-home)
- run ant, this target will create some dirs, compile all source.
- run ant run, this target will run and makes mvnforum.xml files.


- Some bugs when running export:
1. User must have username, password, email, firstemail.
2. TimeStamp format: yyyy:MM:dd ss:mm:hh.
- Import rank:
1. Phpbb's rank_title differ from mvnforum's rank_titleCac rank title cua phpbb khac voi cua mvnforum. 
So I import default rank of mvnforum
- Import group:
1. GlobalPermission: can't find out where phpbb save globalpermission, So I add Globalpermission as default
Guest: 109
Admin: 100
ForumAdmin: 105
member: 110
Forum moderator: 106
2. GlobalPermission: mvnforum co cac global permission cho group:

  Phpbb khong co cac global permission cho group, moi permission cua group duoc gan cu the voi cac forum.
- Import category:
1. Watch: phpbb contains only thread_watch, category_watch, forum_watch can find out using thread_id.

- Export member: write schema for private messages, for avatar.
1. Permission: permission cua phpbb duoc luu trong userlevel.
    a. admin: user_level = 1;
    b. forumModerator = 2;
    c. others = 0;

- Export Category:
    a. Permission: auth_access
        a. Systemadmin: 100;
        b. Forum admin: 105;
        c. Forum Moderator: 106; => auth_mod = 1;
        d. Power user: 111;
        e. Normal user: 110;
        f. Limituser: 109;
        g. edit forum: 2000; => auth_edit
        h. delete forum: 2001; => auth_delete
        . add poll; 2105; => pollcreate;
        . edit poll: 2106; => pollcreate;
        . delete poll: 2107; => pollcreate;
        . get_attachments: 2109; => auth_attachments;
        . add_attachments: 2108;
        . edit post: 2103; =>auth_edit.
        . delete post: 2104; => auth_delete;



Export database mvnforum:

1. Export group;

2. Export rank;
    finish export rank
3. Export member;
4. Export category;

