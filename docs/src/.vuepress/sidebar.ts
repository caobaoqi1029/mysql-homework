import {sidebar} from "vuepress-theme-hope";

export default sidebar({
    "/": [
        "",
        {
            text: "Book",
            icon: "/book.svg",
            prefix: "book/",
            link: "book/",
            children: "structure",
        },
        {
            text: "Homework",
            icon: "/homework.svg",
            link: "Homework.md",
        },
        {
            text: "PROJECT",
            icon: "/project.svg",
            prefix: "project/",
            link: "project/",
            children: "structure",
        },
        {
            text: "CHANGELOG",
            icon: "/update.svg",
            link: "CHANGELOG.md",
        },
    ],
});
