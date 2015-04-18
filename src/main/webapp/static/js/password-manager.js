var PasswordManager = {};

PasswordManager.List = (function () {
    function setup() {
        $('#filter').instaFilta({
            scope: '.passwords',
            targets: '.password-item'
        });

        $('.show-pw').click(function () {
            var $this = $(this);
            $.get($this.data('url'), function (data) {
                var $input = $('#password-' + $this.data('id'));
                $input.val(data);
                $input.attr('type', 'text');
                $this.hide();
                $this.next().show();
            })
        });

        var $hidepw = $('.hide-pw');
        $hidepw.hide();

        $hidepw.click(function () {
            var $this = $(this);
            var $input = $('#password-' + $this.data('id'));
            $input.attr('type', 'password');
            $this.hide();
            $this.prev().show();
        });

        $('.update-pw').click(function () {
            var $this = $(this);
            var $input = $('#password-' + $this.data('id'));
            var val = $input.val();
            $.post($this.data('url'), {pw: val}, function (data) {
                $input.effect( "highlight", {color:"green"}, 3000 );
            })
        });

    }


    function init() {
        $(function () {
            setup();
        });
        return this;
    }

    return {
        init: init
    }

}()).init();