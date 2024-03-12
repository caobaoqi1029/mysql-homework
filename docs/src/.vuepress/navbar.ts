import {navbar} from "vuepress-theme-hope";

export default navbar([
    "/",
    {
        text: "BOOK",
        icon: "/book.svg",
        prefix: "/book",
        children: [
            "",
            {text: "数据库基本知识", icon: "/mysql-item.svg", link: "/mysql01.md"},
        ],
    },
    {
        text: "PROJECT",
        icon: "/project.svg",
        prefix: "/project",
        children: [
            "",
            {text: "学生管理系统", icon: "/note.svg", link: "/project01.md"},
        ],
    },
    {
        text: "Homework",
        icon: "/homework.svg",
        link: "/Homework.md",
    },
    {
        text: "CHANGELOG",
        icon: "/update.svg",
        link: "/CHANGELOG.md",
    },
]);
