$(document).ready(function () {
    if ($('#sumUserOrder').length) {
        let sum = 0;
        $('.orderContainer').each(function() {
            sum += $(this).find('span.price').text() * $(this).find('.count').text();
        });
        $('#sumUserOrder').text(sum);
    }

    $("button.btn.decrement").on("click", function() {
        let difference = updateCount("decbook", $(this).data("id"));
        $('#sumUserOrder').text(Number($('#sumUserOrder').text()) - difference);
    });

    $("button.btn.increment").on("click", function() {
        let difference = updateCount("incbook", $(this).data("id"));
        $('#sumUserOrder').text(Number($('#sumUserOrder').text()) + Number(difference));
    });

    function updateCount(requestPath, index) {
        fetch("/" + requestPath + "?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            if (data.message == "Книга удалена") {
                $('div[data-id="' + index + '"]').remove();
            } else {
                $('span[data-id="' + index + '"]').text(data.item.count);
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
        return $('.orderContainer[data-id="' + index + '"]').find('span.price').text();
    }

    $("button.btn.red.remove").on("click", function() {
        let index = $(this).data("id");
        let price = $('.orderContainer[data-id="' + index + '"]').find('span.price').text();
        let count = $('.orderContainer[data-id="' + index + '"]').find('.count').text();
        fetch("/removebook?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            if (data.message == "Книга успешно удалена") {
                $('div[data-id="' + index + '"]').remove();
                $('#sumUserOrder').text($('#sumUserOrder').text() - (price * count));
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
    });

    $("button.btn.grey.buy").on("click", function() {
        let index = $(this).data("id");
        fetch("/buy?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            console.log(data);
            if (data.message == "Книга добавлена") {
                $('button.btn.grey.buy[data-id="' + index + '"]').text(data.item.count + " шт");
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
    });

    $(".deleteBook").on("click", function() {
        let index = $(this).data("id");
        fetch("/delete?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            console.log(data);
            if (data.message == "Книга успешно удалена") {
                $('div[data-id="' + index + '"]').remove();
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
    });

    if ($(".done").length) {
        $(".done").each(function() {
            if ($('div[data-id="' + $(this).data("id") + '"]').find('.status').text() == "Готово") {
                $(this).prop("disabled",true);
            }
        });
    }

    $(".done").on("click", function() {
        let index = $(this).data("id");
        let buttonDone = $(this);
        fetch("/done?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            if (data.message == "Статус заказа изменен") {
                $('div[data-id="' + index + '"]').find('.status').text(data.status);
                buttonDone.prop("disabled",true);
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
    });

    $(".deleteOrder").on("click", function() {
        let index = $(this).data("id");
        let buttonDelete = $(this);
        fetch("/deleteorder?id=" + index).then(function(response) {
            return response.json();
        }).then(function(data) {
            console.log(data);
            if (data.message == "Заказ удален") {
                $('div[data-id="' + index + '"]').remove();
                buttonDelete.prop("disabled",true);
            }
        }).catch(function(err) {
            console.log('Fetch Error:', err);
        });
    });
});