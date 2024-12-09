if redis.call('get', KEYS[1]) == nil then
	redis.call('set', KEYS[1], ARGV[1]);
	return 1;
end
if redis.call('get', KEYS[1]) ~= ARGV[1] then
	redis.call('set', KEYS[1], ARGV[1]);
	return 1;
else
	return 0;
end;